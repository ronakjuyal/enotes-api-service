package com.enotes.project.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesDto {
    private Integer id;
    private String title;
    private String description;
    private CategoryDto category;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Integer userId;
    @Getter
    @Setter 
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto {
        private Integer id;
        private String name;
        
    }
    

}
