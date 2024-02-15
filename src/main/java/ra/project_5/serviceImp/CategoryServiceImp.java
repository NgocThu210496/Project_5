package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.mapper.MapperCatalog;
import ra.project_5.model.dto.request.CatalogRequest;
import ra.project_5.model.dto.response.CatalogResponse;
import ra.project_5.model.entity.Category;
import ra.project_5.repository.CategoryRepository;
import ra.project_5.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MapperCatalog mapperCatalog;
    @Override
    public List<Category> findAllByStatusIsTrue() {
        return categoryRepository.findAllByStatusIsTrue();
    }

    @Override
    public Category findById(long categoryId) {
        try {
            return categoryRepository.findById(categoryId).get();
        } catch (Exception e){
            throw new CustomException("Category not Exist");
        }
    }

    @Override
    public Page<CatalogResponse> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category>categoryList = categoryPage.getContent();
        List<CatalogResponse> catalogResponses = categoryList.stream()
                .map(category -> mapperCatalog.mapperEntityToResponse(category))
                .collect(Collectors.toList());
        var totalCatalog = categoryPage.getTotalElements();

        return new PageImpl<>(catalogResponses, pageable, totalCatalog);
    }

    @Override
    public CatalogResponse getInForCategoryId(long categoryId) {
        Optional<Category> categoryOptional =categoryRepository.findById(categoryId);
        return categoryOptional.map(category -> mapperCatalog.mapperEntityToResponse(category)).orElse(null);
    }

    @Override
    public CatalogResponse create(CatalogRequest catalogRequest) {
        return mapperCatalog.mapperEntityToResponse(categoryRepository
                .save(mapperCatalog.mapperRequestToEntity(catalogRequest)));
    }

    @Override
    public boolean findCategoryId(long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        return categoryOptional.isPresent();
    }

    @Override
    public CatalogResponse update(CatalogRequest catalogRequest, long catalogId) {
        Optional<Category> categoryOptional = categoryRepository.findById(catalogId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setCategoryName(catalogRequest.getCategoryName());
            category.setDescription(catalogRequest.getDescription());
            category.setStatus(catalogRequest.isStatus());
            return mapperCatalog.mapperEntityToResponse(categoryRepository.save(category));
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if(categoryOptional.isPresent()){
            categoryOptional.get().setStatus(false);
            return categoryRepository.save(categoryOptional.get()).isStatus();
        }else {
            throw new RuntimeException("Category not Exist");
        }
    }
}
