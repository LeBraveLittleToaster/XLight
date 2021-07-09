package de.pschiessle.xlight.xlightserver.service;

import de.pschiessle.xlight.xlightserver.endpoints.reponse.GetAllDevicesResp;
import de.pschiessle.xlight.xlightserver.endpoints.reponse.GetOneDeviceResp;
import de.pschiessle.xlight.xlightserver.repository.XDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class XDeviceService {
    @Autowired
    XDeviceRepository xDeviceRepository;

    public Mono<GetOneDeviceResp> getDeviceById(String deviceId){
        return xDeviceRepository.findOneById(deviceId).map(GetOneDeviceResp::new);
    }

    public GetAllDevicesResp getAllDevices(){
        return new GetAllDevicesResp(List.of());
    }
}
