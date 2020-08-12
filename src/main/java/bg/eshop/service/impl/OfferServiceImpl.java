package bg.eshop.service.impl;

import bg.eshop.domain.entities.Item;
import bg.eshop.domain.entities.Offer;
import bg.eshop.domain.models.service.ItemServiceModel;
import bg.eshop.domain.models.service.OfferServiceModel;
import bg.eshop.repository.OfferRepository;
import bg.eshop.service.ItemService;
import bg.eshop.service.OfferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, ItemService itemService, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OfferServiceModel> getAllOffers() {
        return this.offerRepository
                .findAll()
                .stream()
                .map(offer -> this.modelMapper.map(offer, OfferServiceModel.class))
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 1000000)
    private void produceOffers() {
        this.offerRepository.deleteAll();

        List<ItemServiceModel> items = this.itemService.getAllItems();

        if (items.isEmpty()) {
            return;
        }

        Random random = new Random();
        List<Offer> offers = new ArrayList<>();

        for (int i = 0; i < 5; i ++) {
            Offer offer = new Offer();
            offer.setItem(this.modelMapper.map(items.get(random.nextInt(items.size())), Item.class));
        }
    }
}
