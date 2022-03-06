package by.nosevich.carrental.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class NullAwareBeanUtils extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
    throws IllegalAccessException, InvocationTargetException {
        if (value == null) return;
        super.copyProperty(dest, name, value);
    }

}
