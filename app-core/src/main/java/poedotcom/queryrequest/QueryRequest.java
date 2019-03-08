/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom.queryrequest;

import poedotcom.queryrequest.query.QueryField;
import java.util.ArrayList;
import lombok.Data;
import poedotcom.queryrequest.query.StatusField.StatusValues;
import poedotcom.queryrequest.query.filters.FiltersField;
import poedotcom.queryrequest.query.filters.trade.StashInfoField;
import poedotcom.queryrequest.query.stats.StatsFiltersField;

/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
@Data
public class QueryRequest {
    QueryField query = new QueryField();
    SortField sort = new SortField();

    public static enum Order {
        asc,
        desc
    }
    //this one's for price verification (price check) of Items!
    // the "name" (because most likely the type is part of it)
    //  goes to the "term" field
    public void makeQuery(StatusValues option, String itemname, 
                            FiltersField filters, Order order) {
        query.getStatus().setOption(option);
        //query.setName(itemname);  //TODO: name can be used if you know exactly that it's named like that
        query.setTerm(itemname);
//        query.setType(type);
        //query.addStat(filterRelations);//,statfilters);
        query.setFilters(filters);
        //query.setStash(stash);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    }        
    //this one's for price verification (price check) of Items!
    // the "name" (because most likely the type is part of it)
    //  goes to the "term" field
    public void makeTypeOnlyQuery(StatusValues option, String itemType, 
                            FiltersField filters, Order order) {
        query.getStatus().setOption(option);
        //query.setName(itemname);  //TODO: name can be used if you know exactly that it's named like that
        query.setType(itemType);
//        query.setType(type);
        //query.addStat(filterRelations);//,statfilters);
        query.setFilters(filters);
        //query.setStash(stash);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    }        
    //this one's for price verification (price check) of currency and Gems!
    //the name goes to the type field
    public void makeCurrencyQuery(StatusValues option, String currencyType, 
                            FiltersField filters, Order order) {
        query.getStatus().setOption(option);
        //query.setName(currencyType);
        query.setType(currencyType);
        //query.addStat(filterRelations);//,statfilters);
        query.setFilters(filters);
        //query.setStash(stash);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    }        
    //this one's for price verification (price check) of currency and Gems!
    //the name goes to the type field
    public void makeCurrencyQuery(StatusValues option, String currencyType, Order order) {
        query.getStatus().setOption(option);
        //query.setName(currencyType);
        query.setType(currencyType);
        query.addStat("and");
        //query.addStat(filterRelations);//,statfilters);
        //query.setFilters(filters);
        //query.setStash(stash);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    }        
    public void makeQuery(StatusValues option, String name, String type, String filterRelations, 
            ArrayList<StatsFiltersField> statfilters,FiltersField filters, Order order) {
        query.getStatus().setOption(option);
        query.setName(name);
        query.setType(type);
        query.addStat(filterRelations,statfilters);
        query.setFilters(filters);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    }    
    public void makeQuery(String option, String name, String type, String filterRelations, 
            ArrayList<StatsFiltersField> statfilters,Order order ) {
        query.getStatus().setOption(option);
        query.setName(name);
        query.setType(type);
        query.addStat(filterRelations,statfilters);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";
        sort.setPrice(orderStr);
    } 
    public void makeQuery(String option, String name, String type, String filterRelations, 
            ArrayList<StatsFiltersField> statfilters,FiltersField filters, Order order) {
        query.getStatus().setOption(option);
        query.setName(name);
        query.setType(type);
        query.addStat(filterRelations,statfilters);
        query.setFilters(filters);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    }
    public void makeQuery(String option, String type, String filterRelations, 
            ArrayList<StatsFiltersField> statfilters,FiltersField filters, Order order) {
        query.getStatus().setOption(option);
        //query.setName(name);
        query.setType(type);
        query.addStat(filterRelations,statfilters);
        query.setFilters(filters);
        sort = new SortField();
        String orderStr = order == Order.asc ? "asc" : "desc";        
        sort.setPrice(orderStr);
    } 
//    
//    WORKS like this
//    public void makeQuery() {   //myQuery(16.02.2019-12:13):
//        query.getStatus().setOption("online");
//        query.setName("Vivinsect");
//        query.setType("Unset Ring");
//        query.addStat("and");
//        sort = new SortField();
//        sort.setPrice("asc");
//    }     
}
