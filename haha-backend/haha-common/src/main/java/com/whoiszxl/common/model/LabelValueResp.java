package com.whoiszxl.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 键值对信息
 */
@Data
@NoArgsConstructor
@Schema(description = "键值对信息")
public class LabelValueResp<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "标签", example = "男")
    private String label;

    @Schema(description = "值", example = "1")
    private T value;

    @Schema(description = "是否禁用", example = "false")
    private Boolean disabled;

    @Schema(description = "颜色", example = "#165DFF")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String color;

    public LabelValueResp(String label, T value) {
        this.label = label;
        this.value = value;
    }

    public LabelValueResp(String label, T value, String color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }

    public LabelValueResp(String label, T value, Boolean disabled) {
        this.label = label;
        this.value = value;
        this.disabled = disabled;
    }
}
