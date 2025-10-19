package in.SakshamProject.moneycount.service;


import in.SakshamProject.moneycount.dto.CategoryDto;
import in.SakshamProject.moneycount.entity.CategoryEntity;
import in.SakshamProject.moneycount.entity.ProfileEntity;
import in.SakshamProject.moneycount.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {
private  final ProfileService profileService;

private final CategoryRepo categoryRepo;
//helper methods
    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profile){
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profile)
                .type(categoryDto.getType())
                .build();
    }
    private CategoryDto toDto(CategoryEntity categoryEntity){
        return CategoryDto.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile()!=null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .type(categoryEntity.getType())
                .build();


    }
    public CategoryDto saveCategory(CategoryDto categoryDto){
        ProfileEntity profile=profileService.getCurrentProfile();
        if(categoryRepo.existsByNameAndProfileId(categoryDto.getName(),profile.getId())){
          throw  new RuntimeException("category with name already exists ");
        }
        CategoryEntity newcategory=toEntity(categoryDto,profile);
        newcategory=categoryRepo.save(newcategory);
        return  toDto(newcategory);


    }
    // get categories for current profile

    public List<CategoryDto> getCategoriesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity> categories=categoryRepo.findByProfileId(profile.getId());
        return categories.stream().map(this::toDto).toList();

    }
// get categories by type for the current user
    public  List<CategoryDto> getCategoriesByTypeForCurrentUser(String Type){
        ProfileEntity profile=profileService.getCurrentProfile();
      List<CategoryEntity> categories=  categoryRepo.findByTypeAndProfileId(Type,profile.getId());
      return categories.stream().map(this::toDto).toList();

    }
    public CategoryDto updateCategory(Long categoryid,CategoryDto categoryDto){
        ProfileEntity profile=profileService.getCurrentProfile();
        CategoryEntity existngcat=categoryRepo.findByIdAndProfileId(categoryid,profile.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found or not accessable"));
         existngcat.setName(categoryDto.getName());
         existngcat.setIcon(categoryDto.getIcon());
         // Update type as well
         existngcat.setType(categoryDto.getType());
         existngcat=categoryRepo.save(existngcat);
         return toDto(existngcat);



    }

}
