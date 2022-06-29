package com.bachelorwork.backend.controller;

import com.bachelorwork.backend.algorithm.FilterMandatoryTags;
import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.model.Receiver;
import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.service.ReceiverService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receiver")
@CrossOrigin("*")
public class ReceiverController {

    @Autowired
    ReceiverService receiverService;

    @Autowired
    FilterMandatoryTags filterMandatoryTags;

//    @PostMapping("/addReceiverToUser")
//    public ResponseEntity<?> addReceiver(@RequestParam String receiverName, @RequestParam String username){
//        return receiverService.addReceiver(receiverName, username);
//    }

    @DeleteMapping("/deleteReceiver")
    public ResponseEntity<?> deleteReceiver(@RequestParam String receiverName){
        return receiverService.removeReceiver(receiverName);
    }

    /**
     * @param receiverName - numele receiverului
     * @param tags - label-urile tag urilor
     * @param username-
     * @return ResponseEntity OK - if Receiver and Tags added successfully
     */
    @PostMapping("/addTagsToReceiver")
    public ResponseEntity<?> addTagsToReceiver(@RequestParam String receiverName, @RequestParam String[] tags, @RequestParam String username){
        return receiverService.addReceiverWithTags(receiverName, tags, username);
    }

    //get all receivers with their tags
    @GetMapping( value = "/allTagsFromReceiver")
    public List<Tag> allTagsFromReceiver(@RequestParam Long receiverId, @RequestParam Long userId){
        return receiverService.getAllReceiverTags(receiverId, userId);
    }

    @GetMapping( value = "/allMandatoryTagsFromReceiver")
    public List<Tag> allMandatoryTagsFromReceiver(@RequestParam Long receiverId, @RequestParam Long userId){
        return receiverService.getMandatoryTagsFromReceiver(receiverId, userId);
    }


    /**
     *
     * @param username
     * @return all receivers of an user, with their tags
     */
    @GetMapping(value = "/getAllReceiversOfUser")
    public List<JSONObject> allReceiversOfUser(@RequestParam String username){
        return receiverService.getAllReceiversFromUser(username);
    }
}
