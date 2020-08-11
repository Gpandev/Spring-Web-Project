package bg.eshop.service;

import bg.eshop.domain.models.service.CategoryServiceModel;

import java.util.Collection;

public interface CategoryService {

    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);

    Collection<CategoryServiceModel> getAllCategories();

    CategoryServiceModel getCategoryById(String id);

    CategoryServiceModel getCategoryByName(String name);

    CategoryServiceModel editCategory(String id, CategoryServiceModel categoryServiceModel);

    CategoryServiceModel deleteCategory(String id);
}
