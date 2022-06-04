package com.bachelorwork.backend.service;

import com.bachelorwork.backend.googleDrive.FindFilesByName;
import com.bachelorwork.backend.model.*;
import com.bachelorwork.backend.repository.ICategoryRepository;
import com.bachelorwork.backend.repository.ItemRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ICategoryRepository iCategoryRepository;

    @Autowired
    TagService tagService;

    public List<Item> findAll() {
        return new ArrayList<>(((List<Item>) itemRepository.findAll()));
    }

    public List<Item> findByCategory(Category category){
        if(categoryService.findByCategoryName(category.getName()) == null)
            return null;
        Category existentCategory = categoryService.findByCategoryName(category.getName());
        return new ArrayList<>(itemRepository.findByCategory(existentCategory));
    }

    public List<JSONObject> getItemListsByCategories(){
        JSONObject homeItemsJson = new JSONObject();
        JSONObject beautyItemsJson = new JSONObject();
        JSONObject fashionItemsJson = new JSONObject();
        JSONObject kidsItemsJson = new JSONObject();
        JSONObject sportItemsJson = new JSONObject();
        JSONObject indoorItemsJson = new JSONObject();
        JSONObject adultItemsJson = new JSONObject();

        List<JSONObject> jsonObjects = new ArrayList<>();

        List<Item> homeItems = findByCategory(Category.builder()
                .name("home")
                .build());
        homeItemsJson.put("category", "home");
        homeItemsJson.put("items", homeItems);
        jsonObjects.add(homeItemsJson);

        List<Item> beautyItems = findByCategory(Category.builder()
                .name("beauty")
                .build());
        beautyItemsJson.put("category", "beauty");
        beautyItemsJson.put("items", beautyItems);
        jsonObjects.add(beautyItemsJson);

        List<Item> fashionItems = findByCategory(Category.builder()
                .name("fashion")
                .build());
        fashionItemsJson.put("category", "fashion");
        fashionItemsJson.put("items", fashionItems);
        jsonObjects.add(fashionItemsJson);

        List<Item> kidsItems = findByCategory(Category.builder()
                .name("kids")
                .build());
        kidsItemsJson.put("category", "kids");
        kidsItemsJson.put("items", kidsItems);
        jsonObjects.add(kidsItemsJson);

        List<Item> sportItems = findByCategory(Category.builder()
                .name("sport")
                .build());
        sportItemsJson.put("category", "sport");
        sportItemsJson.put("items", sportItems);
        jsonObjects.add(sportItemsJson);

        List<Item> indoorItems = findByCategory(Category.builder()
                .name("indoor")
                .build());
        indoorItemsJson.put("category", "indoor");
        indoorItemsJson.put("items", indoorItems);
        jsonObjects.add(indoorItemsJson);

        List<Item> adultItems = findByCategory(Category.builder()
                .name("adult")
                .build());
        adultItemsJson.put("category", "adult");
        adultItemsJson.put("items", adultItems);
        jsonObjects.add(adultItemsJson);

        return jsonObjects;

    }

    @Transactional
    public ResponseEntity<?> addItem(Item newItem, String categoryName){
        Item item = new Item();
        Category category = categoryService.findByCategoryName(categoryName);
        if(category == null){
            return new ResponseEntity<>("This category does not exist. Please add first the category",
                    HttpStatus.BAD_REQUEST);
        }
        if(itemRepository.findByItemName(newItem.getItemName()).isPresent()) {
            return new ResponseEntity<>("Item already exists",
                    HttpStatus.OK);
        }
        item.setItemName(newItem.getItemName());
        item.setCategory(category);
        item.setImgName(newItem.getImgName());
        category.getItemList().add(item);
        category.setItemList(category.getItemList());
        itemRepository.save(item);
        iCategoryRepository.save(category);

        return new ResponseEntity<>("item added",
                HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> addItemList(Map<String, List<Item>> itemsToAdd){

        for(Map.Entry<String,List<Item>> entry: itemsToAdd.entrySet()){
            Category category = categoryService.findByCategoryName(entry.getKey());
            if(category == null){
                return new ResponseEntity<>("This category does not exist. Please add first the category",
                        HttpStatus.BAD_REQUEST);
            }
            for(Item item: entry.getValue()){
                if(itemRepository.findByItemName(item.getItemName()).isPresent()) {
                    return new ResponseEntity<>("Item already exists",
                            HttpStatus.OK);
                }
                item.setItemName(item.getItemName());
                item.setCategory(category);
                item.setImgName(item.getImgName());
                category.getItemList().add(item);
                category.setItemList(category.getItemList());
                itemRepository.save(item);
                iCategoryRepository.save(category);
        }
    }
        return new ResponseEntity<>("items added",
                HttpStatus.OK);
    }


    @Transactional
    public Map<String, URL> getItemImageURL(Item item) throws IOException {
        Map<String, URL> itemImageURL = new HashMap<>();
        if(itemRepository.findByItemName(item.getItemName()).isEmpty())
            return null;
        itemImageURL.put(item.getItemName(),FindFilesByName.getGoogleFilesByName(item.getImgName()));
        return itemImageURL;
    }

    static long startTime = System.nanoTime();

    @Transactional
    public List<JSONObject> getItemNameAndImage(List<Item> itemList) throws IOException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        itemList.forEach(item -> {
            JSONObject itemsJSON = new JSONObject();
            itemsJSON.put("itemName", item.getItemName());
            itemsJSON.put("category", item.getCategory().getName());
            try {
                itemsJSON.put("imgURL", FindFilesByName.getGoogleFilesByName(item.getImgName()));
            }
            catch (IndexOutOfBoundsException | IOException indexOutOfBoundsException){
                itemsJSON.put("imgURL", null);
            }
            jsonObjects.add(itemsJSON);
        });
        return jsonObjects;
    }

    static long endTime = System.nanoTime();
    static long duration = (endTime - startTime);



    @Transactional
    public ResponseEntity<?> addTagToItem(Long itemId, String[] tagNames) {
        Optional<Item> item = itemRepository.findById(itemId);
        //Tag tag  = tagService.findByTagName(tagName);

        if (item.isPresent()) {
            Set<Tag> tagSet = new HashSet<Tag>(item.get().getTagList());


            for (String tag : tagNames) {
                Tag tagAux = tagService.findByTagName(tag);
                if (tagAux != null) {
                    tagSet.add(tagAux);
                    }
            }
            List<Tag> choosenTags = new ArrayList<>(tagSet);

            item.get().setTagList(choosenTags);
            itemRepository.save(item.get());
            return new ResponseEntity<>("tags added", HttpStatus.OK);
        } else
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
    }

    public List<Tag> getAllTagsOfItem(Long itemId){
        Item item = itemRepository.findById(itemId).stream().findFirst().orElse(null);
        if(item != null)
            return item.getTagList();
        else
            return null;
    }

    public List<Tag> getMandatoryTagsFromItem(Long itemId){
        List<Tag> allItemTags = getAllTagsOfItem(itemId);
        return allItemTags.stream()
                .filter(tag -> tag.getMandatory() == 1)
                .collect(Collectors.toList());
    }

    public List<Item> filteredItemsByMandatoryTags(List<Tag> selectedTags){
        List<Item> allItems = (List<Item>) itemRepository.findAll();

        List<Tag> mandatoryTags = selectedTags.stream().filter(tag->tag.getMandatory() == 1).collect(Collectors.toList());

        return allItems.stream().
                filter(item -> getMandatoryTagsFromItem(item.getIdItem()).containsAll(mandatoryTags)).collect(Collectors.toList());

    }

}
