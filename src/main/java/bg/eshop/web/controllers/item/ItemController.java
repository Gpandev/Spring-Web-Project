package bg.eshop.web.controllers.item;

import bg.eshop.domain.models.binding.ItemAddBindingModel;
import bg.eshop.domain.models.service.ItemServiceModel;
import bg.eshop.domain.models.view.ItemDetailsViewModel;
import bg.eshop.domain.models.view.ItemsViewModel;
import bg.eshop.error.ItemAlreadyExistsException;
import bg.eshop.error.ItemNotFoundException;
import bg.eshop.service.CategoryService;
import bg.eshop.service.CloudinaryService;
import bg.eshop.service.ItemService;
import bg.eshop.web.PageTitle;
import bg.eshop.web.controllers.BaseController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/items")
public class ItemController extends BaseController {

    private final ItemService itemService;
    private final CloudinaryService cloudinaryService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ItemController(ItemService itemService, CloudinaryService cloudinaryService, CategoryService categoryService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.cloudinaryService = cloudinaryService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Add Item")
    public ModelAndView addProduct() {
        return super.view("item/add-item");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProductConfirm(@ModelAttribute ItemAddBindingModel model) throws IOException {
        ItemServiceModel itemServiceModel = this.modelMapper.map(model, ItemServiceModel.class);

        itemServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));

        this.itemService.createItem(itemServiceModel);

        return super.redirect("/items/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PageTitle("All Items")
    public ModelAndView allProducts(ModelAndView modelAndView) {
        modelAndView.addObject("items", this.itemService.getAllItems()
                .stream()
                .map(p -> this.modelMapper.map(p, ItemsViewModel.class))
                .collect(Collectors.toList()));

        return super.view("item/all-items", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Product Details")
    public ModelAndView detailsProduct(@PathVariable String id, ModelAndView modelAndView) {
        ItemDetailsViewModel model = this.modelMapper.map(this.itemService.getItemById(id), ItemDetailsViewModel.class);

        modelAndView.addObject("item", model);

        return super.view("item/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Edit Item")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView) {
        ItemServiceModel itemServiceModel = this.itemService.getItemById(id);
        ItemAddBindingModel model = this.modelMapper.map(itemServiceModel, ItemAddBindingModel.class);
        // model.setCategories(productServiceModel.getCategories());

        modelAndView.addObject("item", model);
        modelAndView.addObject("itemId", id);

        return super.view("item/edit-item", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editProductConfirm(@PathVariable String id, @ModelAttribute ItemAddBindingModel model) {
        this.itemService.editItem(id, this.modelMapper.map(model, ItemServiceModel.class));

        return super.redirect("/items/details/" + id);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Delete Item")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView) {
        ItemServiceModel productServiceModel = this.itemService.getItemById(id);
        ItemAddBindingModel model = this.modelMapper.map(productServiceModel, ItemAddBindingModel.class);
        // model.setCategories(productServiceModel.getCategories().stream().map(c -> c.getName()).collect(Collectors.toList()));

        modelAndView.addObject("item", model);
        modelAndView.addObject("itemId", id);

        return super.view("item/delete-item", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteProductConfirm(@PathVariable String id) {
        this.itemService.deleteItem(id);

        return super.redirect("/items/all");
    }

    @GetMapping("/fetch/{category}")
    @ResponseBody
    public List<ItemsViewModel> fetchByCategory(@PathVariable String category) {
        if(category.equals("all")) {
            return this.itemService.getAllItems()
                    .stream()
                    .map(item -> this.modelMapper.map(item, ItemsViewModel.class))
                    .collect(Collectors.toList());
        }

        return this.itemService.getAllByCategory(category)
                .stream()
                .map(item -> this.modelMapper.map(item, ItemsViewModel.class))
                .collect(Collectors.toList());

    }

    @ExceptionHandler({ItemNotFoundException.class})
    public ModelAndView handleProductNotFound(ItemNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }

    @ExceptionHandler({ItemAlreadyExistsException.class})
    public ModelAndView handleProductNameALreadyExist(ItemAlreadyExistsException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());

        return modelAndView;
    }
}
