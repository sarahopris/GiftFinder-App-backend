package com.bachelorwork.backend.controller;

import com.bachelorwork.backend.model.Category;
import com.bachelorwork.backend.model.Item;
import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.service.ItemService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.CascadeType;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
@CrossOrigin("*")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping("/addItem")
    public ResponseEntity<?> addItem(@RequestBody Item item, @RequestParam String categoryName){
        return itemService.addItem(item, categoryName);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Item> getItems(){
        return itemService.findAll();
    }

    //map cu numele categoriei
    @GetMapping(value = "/getItemsByCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Item> getItemsByCategory(@RequestBody Category category){
        return itemService.findByCategory(category);
    }

    @GetMapping(value = "/getItemsListsByCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<JSONObject> getItemsListsByCategory(){
        return itemService.getItemListsByCategories();
    }

    @GetMapping(value="/getImageURL")
    public Map<String, URL> getItemImageURL(@RequestBody Item item) throws IOException {
        return itemService.getItemImageURL(item);
    }

    @PostMapping("/addTagsToItem")
    public ResponseEntity<?> addTagsToItem(@RequestParam String item,@RequestParam String[] tags){
        return itemService.addTagToItem(item,tags);
    }

    @PostMapping(value = "/addItems", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTag(@RequestBody Map<String, List<Item>> itemMap){
        return itemService.addItemList(itemMap);
    }
}
