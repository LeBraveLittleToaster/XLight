package de.pschiessle.xlight.xlightserver.endpoints;

import de.pschiessle.xlight.xlightserver.endpoints.reponse.GetOneDeviceResp;
import de.pschiessle.xlight.xlightserver.endpoints.reponse.PutAddDeviceResp;
import de.pschiessle.xlight.xlightserver.endpoints.request.PutAddDeviceReq;
import de.pschiessle.xlight.xlightserver.service.XDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/devices/")
public class XDeviceEndpoints {

    @Autowired
    XDeviceService xDeviceService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Mono<String> getAllDevices() {
        return Mono.just("Hello World");
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public Mono<GetOneDeviceResp> getAllDevices(@PathVariable(value = "deviceId") String deviceId) {
        return xDeviceService.getDeviceById(deviceId).or(Mono.just(new GetOneDeviceResp()));
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public Mono<PutAddDeviceResp> addDevice(@RequestBody PutAddDeviceReq request) {
        System.out.println(request.toString());
        return Mono.just(new PutAddDeviceResp("Some id"));

    }

}
