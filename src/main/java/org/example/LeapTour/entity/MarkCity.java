package org.example.LeapTour.entity;

import lombok.Data;

/**
 * 用于城市点亮模块
 * <p>
 * String email;        用户邮箱
 * String markedCity;   点亮的城市
 */
@Data
public class MarkCity {
    String email;
    String markedCity;
}
