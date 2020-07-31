package bg.eshop.domain.models.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryServiceModel extends BaseServiceModel{

    private String name;

    public CategoryServiceModel() {
    }

    @NotBlank
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
