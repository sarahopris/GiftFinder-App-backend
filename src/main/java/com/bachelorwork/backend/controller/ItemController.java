package com.bachelorwork.backend.controller;

import com.bachelorwork.backend.algorithm.FilterMandatoryTags;
import com.bachelorwork.backend.algorithm.JaccardAlgorithm;
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

    @Autowired
    FilterMandatoryTags filterMandatoryTags;

    @Autowired
    JaccardAlgorithm jaccardAlgorithm;

    @PostMapping("/addItem")
    public ResponseEntity<?> addItem(@RequestBody Item item, @RequestParam String categoryName){
        return itemService.addItem(item, categoryName);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Item> getItems(){
        return itemService.findAll();
    }


    @GetMapping(value = "/getItemsByCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Item> getItemsByCategory(@RequestBody Category category){
        return itemService.findByCategory(category);
    }

//    @GetMapping(value = "/getItemsListsByCategory", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public List<JSONObject> getItemsListsByCategory(){
//        return itemService.getItemListsByCategories();
//    }

//    @GetMapping(value="/getImageURL")
//    public Map<String, URL> getItemImageURL(@RequestBody Item item) throws IOException {
//        return itemService.getItemImageURL(item);
//    }

    @PostMapping("/addTagsToItem")
    public ResponseEntity<?> addTagsToItem(@RequestParam Long itemId,@RequestParam String[] tags){
        return itemService.addTagToItem(itemId,tags);
    }

    @PostMapping(value = "/addItems", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTag(@RequestBody Map<String, List<Item>> itemMap){
        return itemService.addItemList(itemMap);
    }


    /**
     * Method to retrieve items with their image URL and category
     * @return list of JSON objects of type [ {itemName, itemImgURL, category} ]
     * @throws IOException
     */
    @GetMapping(value = "/getItemsLists", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<JSONObject> getItemsLists() throws IOException {
        List<Item> allItems = itemService.findAll();
        return itemService.getItemNameAndImage(allItems);
    }

//    //list mandatoryTags o sa fie dce fapt lista cu toate tag-urile
//    @GetMapping(value = "/getFilteredItems")
//    public List<Item> filteredItems(@RequestParam List<String> selectedTags){
//        return filterMandatoryTags.filteredItemsByMandatoryTags(selectedTags);
//    }

    /**
     *
     * @param selectedTagNames - List of tag labels
     * @return filered elements based on selected tags
     */
    @GetMapping(value= "/getSuggestedItems", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getSuggestedItemsFromAlgorithm(@RequestBody List<String> selectedTagNames) throws IOException {
        List<Item> suggestedItemList = jaccardAlgorithm.resultedItems(selectedTagNames);
        return itemService.getItemNameAndImage(suggestedItemList);
    }

}
