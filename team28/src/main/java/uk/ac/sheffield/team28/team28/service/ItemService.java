package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.repository.ItemRepository;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    //Add Item methods here
}
