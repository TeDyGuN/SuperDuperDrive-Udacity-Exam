package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFilesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{id}")
    File getFileById(Integer id);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    List<File> getFileByFilenameAndUsername(String fileName, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES ( #{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES  WHERE fileid = #{id}")
    int delete(Integer id);
}
