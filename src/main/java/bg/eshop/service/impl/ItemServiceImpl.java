package bg.eshop.service.impl;

import bg.eshop.domain.entities.Category;
import bg.eshop.domain.entities.Item;
import bg.eshop.domain.models.service.ItemServiceModel;
import bg.eshop.error.ItemAlreadyExistsException;
import bg.eshop.error.ItemNotFoundException;
import bg.eshop.repository.ItemRepository;
import bg.eshop.repository.OfferRepository;
import bg.eshop.service.CategoryService;
import bg.eshop.service.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final OfferRepository offerRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(OfferRepository offerRepository, CategoryService categoryService, ModelMapper modelMapper, ItemRepository itemRepository) {
        this.offerRepository = offerRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemServiceModel createItem(ItemServiceModel itemServiceModel) {
        Item item = this.itemRepository.
                findByItemName(itemServiceModel.getName()).
                orElse(null);

        if (item != null) {
            throw new ItemAlreadyExistsException("Item already exists!");
        }

        item = this.modelMapper.map(itemServiceModel, Item.class);

        Category category = new Category();
        this.modelMapper.map(categoryService.getCategoryById(itemServiceModel.getCategories()), category);

        item.setCategories(category);
        item = this.itemRepository.save(item);

        return this.modelMapper.map(item, ItemServiceModel.class);
    }

    @Override
    public List<ItemServiceModel> getAllItems() {
        return this.itemRepository.
                findAll().
                stream().
                map(item -> this.modelMapper.map(item, ItemServiceModel.class)).
                collect(Collectors.toList());
    }

    @Override
    public ItemServiceModel getItemById(String id) {
        return this.itemRepository.findById(id).
                map(item -> {
                    ItemServiceModel itemServiceModel = this.modelMapper.map(item, ItemServiceModel.class);
                    this.offerRepository.findByItem_Id(itemServiceModel.getId()).
                            ifPresent(p -> itemServiceModel.setDiscountedPrice(p.getPrice()));
                    itemServiceModel.setCategories(item.getCategories().getName());

                    return itemServiceModel;
                }).orElseThrow(() -> new ItemNotFoundException("Item not exists."));
    }

    @Override
    public ItemServiceModel editItem(String id, ItemServiceModel itemServiceModel) {
        Item item = this.itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not exists."));

        Category category = new Category();
        category.setName(itemServiceModel.getCategories());

        item.setItemName(itemServiceModel.getName());
        item.setDescription(itemServiceModel.getDescription());
        item.setPrice(itemServiceModel.getPrice());

        this.modelMapper.map(categoryService.getCategoryById(itemServiceModel.getCategories()), category);
        item.setCategories(category);

        this.offerRepository.findByItem_Id(item.getId())
                .ifPresent((b) -> {
                    b.setPrice(item.getPrice().multiply(new BigDecimal("0.75")));

                    this.offerRepository.save(b);
                });

        return this.modelMapper.map(this.itemRepository.saveAndFlush(item), ItemServiceModel.class);
    }

    @Override
    public void deleteItem(String id) {
        Item item = this.itemRepository.findById(id).orElseThrow((() -> new ItemNotFoundException("Item not exists.")));

        this.itemRepository.delete(item);
    }

    @Override
    public List<ItemServiceModel> getAllByCategory(String category) {
        return this.itemRepository.
                findAll().
                stream().
                filter(item -> item.getCategories().getName().equals(category)).
                map(item -> this.modelMapper.map(item, ItemServiceModel.class))
                .collect(Collectors.toList());
    }
}
