package com.smaato.assigment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/smaato")
public class ApplicationController {

    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private List<Integer> requestIds = new ArrayList<>();
    private int requestCount = 0;

    @GetMapping(value = "/accept")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity accept(@RequestParam(name = "id", required = true) int id,
                                             @RequestParam(name = "endpoint", required = false) String endpoint) {

        try {
            if (!requestIds.contains(id)) {
                requestIds.add(id);
                requestCount++;
            }

            URI uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http").host("127.0.0.1")
                    .path("/api/smaato/accept").query("id={id}").query("endpoint={endpoint}").buildAndExpand(id, "ok").toUri();

            return ResponseEntity.status(HttpStatus.OK).location(uriComponents).build();
        }catch (Exception exception){
            URI uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http").host("127.0.0.1")
                    .path("/api/smaato/accept").query("id={id}").query("endpoint={endpoint}").buildAndExpand(id, "failed").toUri();

            return ResponseEntity.status(HttpStatus.OK).location(uriComponents).build();
        }
    }


    @Scheduled(fixedRate = 60000)
    private void printLogs(){
        logger.info("Number Of Unique Requests this minute : " + requestCount);
        requestCount=0;
        requestIds.clear();
    }

}
