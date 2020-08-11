package bg.eshop.service;

import bg.eshop.domain.models.service.OfferServiceModel;

import java.util.List;

public interface OfferService {

    List<OfferServiceModel> getAllOffers();
}
