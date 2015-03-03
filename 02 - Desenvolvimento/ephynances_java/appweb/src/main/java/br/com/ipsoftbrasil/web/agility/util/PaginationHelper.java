package br.com.ipsoftbrasil.web.agility.util;

import br.com.ipsoftbrasil.web.agility.dao.DAO;
import br.com.ipsoftbrasil.web.agility.model.BaseModel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

public class PaginationHelper<T extends BaseModel> extends LazyDataModel<T> implements Serializable, SelectableDataModel<T> {

    private static final long serialVersionUID = 165156156L;

    private List<T> itens;

    private DAO<T> dao;

    private PaginationCompleteItens completeItens;

    private String defaultSortField;

    private Map<String, String> defaultFilters;

    private Map<String, String> globalFilters;

    public PaginationHelper(DAO<T> dao) {
        this.dao = dao;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        boolean asc = (sortOrder == SortOrder.ASCENDING);

        if (sortField == null) {
            sortField = defaultSortField;
        }

        if (defaultFilters != null) {
            filters = defaultFilters;
        }

        if (filters.containsKey("globalFilter")) {
            filters.remove("globalFilter");
        }

        itens = dao.findRange(first, pageSize, sortField, asc, filters, globalFilters);

        completeItens(itens);

        setRowCount(dao.count(filters, globalFilters));

        return itens;
    }

    public List<T> getDatasource() {
        return itens;
    }

    @Override
    public void setRowIndex(int rowIndex) {

        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        } else {
            super.setRowIndex(rowIndex % getPageSize());
        }
    }

    @Override
    public Object getRowKey(T item) {
        return item.getId();
    }

    @Override
    public T getRowData(String itemId) {
        Integer id = Integer.valueOf(itemId);

        for (T player : itens) {
            if (id.equals(player.getId())) {
                return player;
            }
        }

        return null;
    }

    private void completeItens(List<T> itens) {
        if (completeItens != null) {
            for (T item : itens) {

                completeItens.complete(item);
            }
        }
    }

    public PaginationCompleteItens getCompleteItens() {
        return completeItens;
    }

    public void setCompleteItens(PaginationCompleteItens completeItens) {
        this.completeItens = completeItens;
    }

    public String getDefaultSortField() {
        return defaultSortField;
    }

    public void setDefaultSortField(String defaultSortField) {
        this.defaultSortField = defaultSortField;
    }

    public Map<String, String> getDefaultFilters() {
        return defaultFilters;
    }

    public void setDefaultFilters(Map<String, String> defaultFilters) {
        this.defaultFilters = defaultFilters;
    }

    public Map<String, String> getGlobalFilters() {
        return globalFilters;
    }

    public void setGlobalFilters(Map<String, String> globalFilters) {
        this.globalFilters = globalFilters;
    }
}
