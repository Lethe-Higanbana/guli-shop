package com.summersky.gulisop.search.service;

import com.summersky.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author Lenovo
 * @Authur:zengfanbin
 * @Date:2020-7-15
 * @Time:15:45
 * @Description:
 */
public interface ProductSaveService {

    Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
