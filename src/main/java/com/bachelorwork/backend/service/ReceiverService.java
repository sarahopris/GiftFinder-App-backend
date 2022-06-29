package com.bachelorwork.backend.service;

import com.bachelorwork.backend.model.*;
import com.bachelorwork.backend.repository.IReceiverRepository;
import com.bachelorwork.backend.repository.ITagRepository;
import com.bachelorwork.backend.repository.IUserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiverService {

    @Autowired
    IReceiverRepository iReceiverRepository;

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    ITagRepository iTagRepository;

    @Autowired
    TagService tagService;



    public ResponseEntity<?> removeReceiver(String receiverName){
        Receiver receiver = findByReceiverName(receiverName);
        //Optional<User> user = iUserRepository.findUserByUsername(username);

        if(receiver != null){
            iReceiverRepository.delete(receiver);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public Receiver findByReceiverName(String receiverName){
        return iReceiverRepository.findByName(receiverName).stream().findFirst().orElse(null);
    }
    @Transactional
    public ResponseEntity<?> addReceiverWithTags(String receiverName, String[] tagNames, String username) {
        Receiver receiver = new Receiver();
        Optional<User> user = iUserRepository.findUserByUsername(username);
        if(user.isEmpty()){
            return new ResponseEntity<>("The user is null",
                    HttpStatus.BAD_REQUEST);
        }

        if(user.get().getReceiversList().stream().anyMatch(receiverElem -> receiverElem.getName().equals(receiverName))) {

            return new ResponseEntity<>("This receiver already exists",
                    HttpStatus.BAD_REQUEST);
        }
        Set<Tag> tagSet = new HashSet<>(receiver.getTagList());

        for (String tag : tagNames) {
            Tag tagAux = tagService.findByTagName(tag);
            if (tagAux != null) {
                tagSet.add(tagAux);
            }
        }

        List<Tag> allTags = new ArrayList<>(tagSet);

        receiver.setName(receiverName);
        receiver.setUser(user.get());
        user.get().getReceiversList().add(receiver);
        user.get().setReceiversList(user.get().getReceiversList());
        if(!allTags.isEmpty()) {
            receiver.setTagList(allTags);
            iReceiverRepository.save(receiver);
            iUserRepository.save(user.get());
        }
        else {
            return new ResponseEntity<>("No tags selected", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("tags added", HttpStatus.OK);

    }


    public List<Tag> getAllReceiverTags(Long receiverId, Long userId){
        Optional<User> user = iUserRepository.findById(userId);
        Receiver receiver;
        if(user.isPresent()) {
            receiver = user.get().getReceiversList().stream()
                    .filter(receiverElem -> receiverElem.getId().equals(receiverId))
                    .findFirst().orElse(null);
            if(receiver != null)
                return receiver.getTagList();

        }
        return null;
    }

    public List<Tag> getMandatoryTagsFromReceiver(Long receiverId, Long userId){
        List<Tag> allTagsOfReceiver = getAllReceiverTags(receiverId, userId);
        return allTagsOfReceiver.stream().filter(tag -> tag.getMandatory()==1).collect(Collectors.toList());

    }


    public List<JSONObject> getAllReceiversFromUser(String username){
        Optional<User> user = iUserRepository.findUserByUsername(username);
        List<JSONObject> jsonList = new ArrayList<>();

        user.ifPresent(value -> value.getReceiversList().forEach(receiver -> {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("receiverName", receiver.getName());
            jsonObj.put("tags", receiver.getTagList());
            jsonList.add(jsonObj);
        }));

        return jsonList;
    }
}
