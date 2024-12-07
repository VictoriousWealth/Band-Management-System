package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Item;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;

import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    //Add Item methods here
    public Item findById(Long id){
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()){
            return item.get();

        } else {
            throw new IllegalArgumentException("Item not found.");
        }
    }
}
