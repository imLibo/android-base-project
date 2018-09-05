package com.cnksi.android.safe;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cnksi.android.log.KLog;

/**
 * @author Wastrel
 * @version 1.0
 * @date 2018/2/5 14:44
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class XSSLayoutFactory implements LayoutInflater.Factory2 {

    public interface FilterRegex {
        SparseArray<String> getRegexMap();
    }

    private AppCompatDelegate appCompatDelegate;
    private LayoutInflater inflater;
    private FilterRegex mFilterRegex;

    private XSSLayoutFactory(AppCompatDelegate appCompatDelegate, LayoutInflater inflater, FilterRegex mFilterRegex) {
        this.appCompatDelegate = appCompatDelegate;
        this.inflater = inflater;
        this.mFilterRegex = mFilterRegex;
        if (inflater.getFactory2() == null) {
            inflater.setFactory2(this);
        } else {
            throw new InflateException("inflater has a LayoutFactory!!!");
        }
    }

    public static XSSLayoutFactory installViewFactory(AppCompatDelegate appCompatDelegate, LayoutInflater inflater, FilterRegex mFilterRegex) {
        return new XSSLayoutFactory(appCompatDelegate, inflater, mFilterRegex);
    }

    public static void setEditTextFilter(EditText editText, String symbolRegex, String letterRegex) {
        InputFilter[] filters = editText.getFilters();
        boolean hasLengthFilter = false;
        for (InputFilter filter : filters) {
            if (filter instanceof InputFilter.LengthFilter) {
                hasLengthFilter = true;
                break;
            }
        }
        InputFilter[] inputFilters = new InputFilter[hasLengthFilter ? filters.length + 1 : filters.length + 2];
        System.arraycopy(filters, 0, inputFilters, 0, filters.length);

        inputFilters[filters.length] = (source, start, end, dest, dstart, dend) -> {
            boolean isAllowSymbol = SafeUtil.allowSymbol(source.toString(), symbolRegex);
            boolean isAllowLetter = SafeUtil.isLetterDigitOrChinese(source.toString(), letterRegex);
            if (isAllowSymbol || isAllowLetter) {
                return null;
            } else {
                return "";
            }
        };
        if (!hasLengthFilter) {
            inputFilters[filters.length + 1] = new InputFilter.LengthFilter(100);
        }
        editText.setFilters(inputFilters);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //先调用AppCompat的处理逻辑
        View result = appCompatDelegate.createView(parent, name, context, attrs);
        //如果是系统控件则直接New一个出来
        if (result == null) {
            if ("EditText".equals(name)) {
                result = new EditText(context, attrs);
            }
        }
        //如果是自定义控件 则先检查是否是我们要处理的子类，如果是子类则调用inflate加载出来。
        if (result == null && name.contains(".")) {
            try {
                Class clz = Class.forName(name);
                if (EditText.class.isAssignableFrom(clz)) {
                    result = inflater.createView(name, null, attrs);
                }
            } catch (ClassNotFoundException e) {
                KLog.e(e);
            }
        }
        //判断属于我们要处理的类型 然后处理
        if (EditText.class.isInstance(result)) {
            String regex = "";
            if (mFilterRegex != null) {
                SparseArray<String> regexMap = mFilterRegex.getRegexMap();
                regex = regexMap != null ? regexMap.get(result.getId()) : "";
            }
            setEditTextFilter((EditText) result, regex, regex);
        }
        return result;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }
}