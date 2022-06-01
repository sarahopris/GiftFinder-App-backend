package com.bachelorwork.backend.service;

import com.bachelorwork.backend.model.*;
import com.bachelorwork.backend.repository.IReceiverRepository;
import com.bachelorwork.backend.repository.ITagRepository;
import com.bachelorwork.backend.repository.IUserRepository;
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

    /**
     * creates new Object of type Receiver and adds it to the User to which belongs to
     * @param receiverName
     * @param username
     * @return response entity OK if the Receiver was successfully added
     * */

    @Transactional
    public ResponseEntity<?> addReceiver(String receiverName, String username){
        Item item = new Item();
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
        receiver.setName(receiverName);
        receiver.setUser(user.get());
        user.get().getReceiversList().add(receiver);
        user.get().setReceiversList(user.get().getReceiversList());
        iReceiverRepository.save(receiver);
        iUserRepository.save(user.get());

        return new ResponseEntity<>(HttpStatus.OK);

    }

    public ResponseEntity<?> removeReceiver(String receiverName, String username){
        Receiver receiver = findByReceiverName(receiverName);
        Optional<User> user = iUserRepository.findUserByUsername(username);

        if(receiver != null && user.isPresent()){
            User userObj = user.get();
            if(userObj.getReceiversList().contains(receiver)) {
                userObj.getReceiversList().remove(receiver);
                iReceiverRepository.delete(receiver);
                iUserRepository.save(userObj);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else return new ResponseEntity<>("User doesn't have this receiver", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public Receiver findByReceiverName(String receiverName){
        return iReceiverRepository.findByName(receiverName).stream().findFirst().orElse(null);
    }



    @Transactional
    public ResponseEntity<?> addTagToReceiver(String receiverName, String[] tagNames, String username) {
        Optional<User> user = iUserRepository.findUserByUsername(username);
        //Tag tag  = tagService.findByTagName(tagName);
        //Receiver receiver = Receiver.builder().name(receiverName).build();

        if(user.isPresent()) {
            Receiver receiver = user.get().getReceiversList().stream().filter(receiverElem -> receiverElem.getName().equals(receiverName)).findFirst().orElse(null);

            if (receiver != null) {
                Set<Tag> tagSet = new HashSet<>(receiver.getTagList());

                for (String tag : tagNames) {
                    Tag tagAux = tagService.findByTagName(tag);
                    if (tagAux != null) {
                        tagSet.add(tagAux);
                    }
                }

                List<Tag> allTags = new ArrayList<>(tagSet);
                receiver.setTagList(allTags);
                iReceiverRepository.save(receiver);
                return new ResponseEntity<>("tags added", HttpStatus.OK);
            } else
                return new ResponseEntity<>("User does not have receiver", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
}
