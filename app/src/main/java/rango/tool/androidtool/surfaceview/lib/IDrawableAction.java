package rango.tool.androidtool.surfaceview.lib;

import java.util.List;

public interface IDrawableAction {

    void update(List<? extends IDrawableBean> drawableBeanList);

    void append(List<? extends IDrawableBean> drawableBeanList);

    void append(IDrawableBean drawableBean);

    void remove(IDrawableBean drawableBean);
}
