package ynu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ynu.annotation.AuthInterceptor;
import ynu.entity.Category;
import ynu.enums.InterceptorLevel;
import ynu.service.ICategoryService;
import ynu.util.ControllerUtils;


@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
    private ICategoryService categoryService;

    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam("name") String name) {
        return ControllerUtils.getResponse(categoryService.insert(name));
    }


    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String update(@PathVariable("id") int id, String name) {
        boolean isSuccess = name!=null&&name!=""&& categoryService.update(id, name);
        return ControllerUtils.getResponse(isSuccess);
    }

    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String remove(@PathVariable("id") int id) {
        return ControllerUtils.getResponse(categoryService.remove(id));
    }

    
    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getById(@PathVariable("id") int id) {
        Category category = categoryService.getById(id);
        if (category==null) {
            return ControllerUtils.getResponse(false);
        } else {
            return category.toString();
        }
    }

    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getAll() {
        return ControllerUtils.listToJson(categoryService.list());
    }
}
