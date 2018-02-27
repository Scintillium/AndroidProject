package classes;

/**
 * Created by Ewan on 2016/12/13.
 */

public class Filter {
    public String value;
    public FilterType type;

    public Filter() {
        this.value = "";
        this.type = FilterType.NONE;
    }

    public Filter(FilterType filter, String val) {
        this.value = val;
        this.type = filter;
    }

}
