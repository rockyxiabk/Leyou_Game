package com.leyou.game.bean.win;

/**
 * Description : 赢大奖奖励说明
 *
 * @author : rocky
 * @Create Time : 2017/10/27 下午12:08
 * @Modified Time : 2017/10/27 下午12:08
 */
public class WinGameAwardBean {

    public String imgUrl;
    public String imgSmallUrl;
    public String name;
    public int price;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgSmallUrl() {
        return imgSmallUrl;
    }

    public void setImgSmallUrl(String imgSmallUrl) {
        this.imgSmallUrl = imgSmallUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
