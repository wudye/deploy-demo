package com.mwu.geodistance.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * A wrapper around Spring Data's {@link Page} to standardize API pagination responses.
 *
 * @param <T> the type of content in the page
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPage<T> {

    private List<T> content;

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalElementCount;

    private Integer totalPageCount;

    /**
     * Converts a Spring {@link Page} into a {@link CustomPage}, preserving pagination metadata.
     *
     * @param domainModels the content to be returned
     * @param page the source Spring page object
     * @param <C> the type of the response content
     * @param <X> the type of the original page content
     * @return a {@link CustomPage} instance
     */

    /*
    static：方法属于类（可用 ClassName.of(...) 调用），不需要类实例。
<C, X>：在返回类型之前声明的方法级泛型参数。它只是声明了两个类型参数的名字，表示方法内部会用到这两个类型。
方法实际返回的是 CustomPage<C>，也就是说返回值使用的是 C（作为页面内容的类型）。
X 只是用在参数 Page<X> 上，表示源 Page 中的元素类型，但不会出现在返回类型中。
方法级泛型是必须的，因为静态方法不能直接使用类的类型参数，且这里需要同时表示两种不同的类型关系（输入内容类型和源 Page 的元素类型）。
     */
    public static <C, X> CustomPage<C> of(final List<C> domainModels, final Page<X> page) {
      /*
      式的类型见证（type witness）。.<C> 强制把 builder 的泛型设为 C，确保返回的 Builder 和最终的 build()
      使用 CustomPage<C>。有时编译器能根据参数推断类型可以省略，但写上可以避免歧义或推断失败。
       */
        return CustomPage.<C>builder()
                .content(domainModels)
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPageCount(page.getTotalPages())
                .totalElementCount(page.getTotalElements())
                .build();
    }

}


