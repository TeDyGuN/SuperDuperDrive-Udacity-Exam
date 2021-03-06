package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES ( #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES  SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(Note note);

    @Delete("DELETE FROM NOTES  WHERE noteid = #{id}")
    int delete(Integer id);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle} AND notedescription = #{noteDescription}")
    List<Note> getNotesByTitleAndDescription(String noteTitle, String noteDescription);
}
