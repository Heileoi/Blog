package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 文章Mapper接口
 * 功能：文章表的数据访问层，包含自定义查询方法
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询文章归档（按年月分组统计）
     * @return 归档列表，包含year、month、count字段
     */
    @Select("SELECT YEAR(publish_time) as year, MONTH(publish_time) as month, COUNT(*) as count " +
            "FROM tb_article WHERE status = 1 AND is_deleted = 0 " +
            "GROUP BY YEAR(publish_time), MONTH(publish_time) " +
            "ORDER BY year DESC, month DESC")
    List<Map<String, Object>> selectArchives();

    /**
     * 增加文章浏览量
     * @param articleId 文章ID
     */
    @Select("UPDATE tb_article SET view_count = view_count + 1 WHERE id = #{articleId}")
    int incrementViewCount(@Param("articleId") Long articleId);

    /**
     * 增加文章点赞数
     * @param articleId 文章ID
     */
    @Select("UPDATE tb_article SET like_count = like_count + 1 WHERE id = #{articleId}")
    int incrementLikeCount(@Param("articleId") Long articleId);
}
