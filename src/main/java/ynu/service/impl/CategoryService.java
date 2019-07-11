package ynu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ynu.dao.CategoryDao;
import ynu.entity.Category;
import ynu.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService{
	
	@Autowired
    private CategoryDao categoryDao;

    public boolean insert(String name) {
        return name!=null && categoryDao.insertCategory(name);
    }


    public boolean remove(int id) {
        return categoryDao.removeCategoryById(id);
    }

    public boolean update(int id, String name) {
        return categoryDao.updateNameById(id, name);
    }

    public Category getById(int id) {
        return categoryDao.getCategoryById(id);
    }


    public List<Category> list() {
        return categoryDao.listCategory();
    }

	public int getIdByName(String name) {
		
		return 0;
	}

}
