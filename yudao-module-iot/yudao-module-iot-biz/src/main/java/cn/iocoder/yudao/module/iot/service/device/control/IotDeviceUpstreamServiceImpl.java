package cn.iocoder.yudao.module.iot.service.device.control;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.object.ObjectUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.iot.api.device.dto.control.upstream.IotDeviceEventReportReqDTO;
import cn.iocoder.yudao.module.iot.api.device.dto.control.upstream.IotDevicePropertyReportReqDTO;
import cn.iocoder.yudao.module.iot.api.device.dto.control.upstream.IotDeviceStateUpdateReqDTO;
import cn.iocoder.yudao.module.iot.api.device.dto.control.upstream.IotDeviceUpstreamAbstractReqDTO;
import cn.iocoder.yudao.module.iot.controller.admin.device.vo.control.IotDeviceSimulationUpstreamReqVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.device.IotDeviceDO;
import cn.iocoder.yudao.module.iot.enums.device.IotDeviceMessageIdentifierEnum;
import cn.iocoder.yudao.module.iot.enums.device.IotDeviceMessageTypeEnum;
import cn.iocoder.yudao.module.iot.enums.device.IotDeviceStateEnum;
import cn.iocoder.yudao.module.iot.mq.message.IotDeviceMessage;
import cn.iocoder.yudao.module.iot.mq.producer.device.IotDeviceProducer;
import cn.iocoder.yudao.module.iot.service.device.IotDeviceService;
import cn.iocoder.yudao.module.iot.service.device.data.IotDevicePropertyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 设备上行 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class IotDeviceUpstreamServiceImpl implements IotDeviceUpstreamService {

    @Resource
    private IotDeviceService deviceService;
    @Resource
    private IotDevicePropertyService devicePropertyService;

    @Resource
    private IotDeviceProducer deviceProducer;

    @Override
    @SuppressWarnings("unchecked")
    public void simulationDeviceUpstream(IotDeviceSimulationUpstreamReqVO simulatorReqVO) {
        // 1. 校验存在
        IotDeviceDO device = deviceService.validateDeviceExists(simulatorReqVO.getId());

        // 2.1 情况一：属性上报
        String requestId = IdUtil.fastSimpleUUID();
        if (Objects.equals(simulatorReqVO.getType(), IotDeviceMessageTypeEnum.PROPERTY.getType())) {
            reportDeviceProperty(((IotDevicePropertyReportReqDTO)
                    new IotDevicePropertyReportReqDTO().setRequestId(requestId).setReportTime(LocalDateTime.now())
                            .setProductKey(device.getProductKey()).setDeviceName(device.getDeviceName()))
                    .setProperties((Map<String, Object>) simulatorReqVO.getData()));
            return;
        }
        // 2.2 情况二：事件上报
        if (Objects.equals(simulatorReqVO.getType(), IotDeviceMessageTypeEnum.EVENT.getType())) {
            // TODO 芋艿：待实现
            return;
        }
        // 2.3 情况三：状态变更
        if (Objects.equals(simulatorReqVO.getType(), IotDeviceMessageTypeEnum.STATE.getType())) {
            updateDeviceState(((IotDeviceStateUpdateReqDTO)
                    new IotDeviceStateUpdateReqDTO().setRequestId(IdUtil.fastSimpleUUID()).setReportTime(LocalDateTime.now())
                            .setProductKey(device.getProductKey()).setDeviceName(device.getDeviceName()))
                    .setState((Integer) simulatorReqVO.getData()));
            return;
        }
        throw new IllegalArgumentException("未知的类型：" + simulatorReqVO.getType());
    }

    @Override
    public void updateDeviceState(IotDeviceStateUpdateReqDTO updateReqDTO) {
        Assert.isTrue(ObjectUtils.equalsAny(updateReqDTO.getState(),
                IotDeviceStateEnum.ONLINE.getState(), IotDeviceStateEnum.OFFLINE.getState()),
                "状态不合法");
        // 1.1 获得设备
        log.info("[updateDeviceState][更新设备状态: {}]", updateReqDTO);
        IotDeviceDO device = deviceService.getDeviceByProductKeyAndDeviceNameFromCache(
                updateReqDTO.getProductKey(), updateReqDTO.getDeviceName());
        if (device == null) {
            log.error("[updateDeviceState][设备({}/{}) 不存在]",
                    updateReqDTO.getProductKey(), updateReqDTO.getDeviceName());
            return;
        }
        // 1.2 记录设备的最后时间
        updateDeviceLastTime(device, updateReqDTO);
        // 1.3 当前状态一致，不处理
        if (Objects.equals(device.getState(), updateReqDTO.getState())) {
            return;
        }

        // 2. 更新设备状态
        TenantUtils.executeIgnore(() ->
                deviceService.updateDeviceState(device.getId(), updateReqDTO.getState()));

        // 3. TODO 芋艿：子设备的关联

        // 4. 发送设备消息
        IotDeviceMessage message = BeanUtils.toBean(updateReqDTO, IotDeviceMessage.class)
                .setType(IotDeviceMessageTypeEnum.STATE.getType())
                .setIdentifier(ObjUtil.equals(updateReqDTO.getState(), IotDeviceStateEnum.ONLINE.getState())
                        ? IotDeviceMessageIdentifierEnum.STATE_ONLINE.getIdentifier()
                        : IotDeviceMessageIdentifierEnum.STATE_OFFLINE.getIdentifier());
        sendDeviceMessage(message, device);
    }

    @Override
    public void reportDeviceProperty(IotDevicePropertyReportReqDTO reportReqDTO) {
        // 1.1 获得设备
        log.info("[reportDevicePropertyData][上报设备属性数据: {}]", reportReqDTO);
        IotDeviceDO device = deviceService.getDeviceByProductKeyAndDeviceNameFromCache(
                reportReqDTO.getProductKey(), reportReqDTO.getDeviceName());
        if (device == null) {
            log.error("[reportDevicePropertyData][设备({}/{})不存在]",
                    reportReqDTO.getProductKey(), reportReqDTO.getDeviceName());
            return;
        }
        // 1.2 记录设备的最后时间
        updateDeviceLastTime(device, reportReqDTO);

        // 2. 发送设备消息
        IotDeviceMessage message = BeanUtils.toBean(reportReqDTO, IotDeviceMessage.class)
                .setType(IotDeviceMessageTypeEnum.PROPERTY.getType())
                .setIdentifier(IotDeviceMessageIdentifierEnum.PROPERTY_REPORT.getIdentifier())
                .setData(reportReqDTO.getProperties());
        sendDeviceMessage(message, device);
    }

    @Override
    public void reportDeviceEvent(IotDeviceEventReportReqDTO reportReqDTO) {
        log.info("[reportDeviceEventData][上报设备事件数据: {}]", reportReqDTO);

        // TODO 芋艿：待实现
    }

    private void updateDeviceLastTime(IotDeviceDO device, IotDeviceUpstreamAbstractReqDTO reqDTO) {
        // 1. TODO 芋艿：插件状态

        // 2. 更新设备的最后时间
        devicePropertyService.updateDeviceReportTime(device.getDeviceKey(), LocalDateTime.now());
    }

    private void sendDeviceMessage(IotDeviceMessage message, IotDeviceDO device) {
        // 1. 完善消息
        message.setDeviceKey(device.getDeviceKey());
        if (StrUtil.isEmpty(message.getRequestId())) {
            message.setRequestId(IdUtil.fastSimpleUUID());
        }
        if (message.getReportTime() == null) {
            message.setReportTime(LocalDateTime.now());
        }

        // 2. 发送消息
        try {
            deviceProducer.sendDeviceMessage(message);
            log.info("[sendDeviceMessage][message({}) 发送消息成功]", message);
        } catch (Exception e) {
            log.error("[sendDeviceMessage][message({}) 发送消息失败]", message, e);
        }
    }

}
