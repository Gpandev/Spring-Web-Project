package bg.eshop.validation.category;

import bg.eshop.domain.models.binding.CategoryEditBindingModel;
import bg.eshop.repository.CategoryRepository;
import bg.eshop.validation.ValidationConstants;
import bg.eshop.validation.annotation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

@Validator
public class CategoryEditValidator implements org.springframework.validation.Validator {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryEditValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CategoryEditBindingModel categoryEditBindingModel = (CategoryEditBindingModel) o;

        if (categoryEditBindingModel.getName().length() < 3) {
            errors.rejectValue(
                    "name",
                    ValidationConstants.NAME_LENGTH,
                    ValidationConstants.NAME_LENGTH
            );
        }

        if (this.categoryRepository.findByName(categoryEditBindingModel.getName()).isPresent()) {
            errors.rejectValue(
                    "name",
                    String.format(ValidationConstants.NAME_ALREADY_EXISTS, "Category", categoryEditBindingModel.getName()),
                    String.format(ValidationConstants.NAME_ALREADY_EXISTS, "Category", categoryEditBindingModel.getName())
            );
        }
    }
}
