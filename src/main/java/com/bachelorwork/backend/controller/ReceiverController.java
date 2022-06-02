package com.bachelorwork.backend.controller;

import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.model.Receiver;
import com.bachelorwork.backend.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receiver")
@CrossOrigin("*")
public class ReceiverController {

    @Autowired
    ReceiverService receiverService;

    @PostMapping("/addReceiverToUser")
    public ResponseEntity<?> addReceiver(@RequestParam String receiverName, @RequestParam String username){
        return receiverService.addReceiver(receiverName, username);
    }



    @DeleteMapping("/deleteReceiver")
    public ResponseEntity<?> deleteReceiver(@RequestParam String receiverName){
        return receiverService.removeReceiver(receiverName);
    }

    /**
     * @param receiverName
     * @param tags
     * @param username
     * @return ResponseEntity OK - if Receiver and Tags added successfully
     */
    @PostMapping("/addTagsToReceiver")
    public ResponseEntity<?> addTagsToReceiver(@RequestParam String receiverName, @RequestParam String[] tags, @RequestParam String username){
        return receiverService.addTagToReceiver(receiverName, tags, username);
    }
}
