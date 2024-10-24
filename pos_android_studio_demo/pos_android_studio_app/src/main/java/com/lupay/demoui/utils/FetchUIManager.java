package com.lupay.demoui.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.Nullable;

import com.lupay.demoui.interfaces.FetchEntity;
import com.lupay.demoui.interfaces.FetchOptions;
import com.lupay.demoui.interfaces.IFetchs;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;

/**
 * Clase general que maneja multiples Llamadas al backend en un activity
 */
public class FetchUIManager implements IFetchs {

    public enum FetchResultType {
        SUCCESS, PROCESSING, FAILED
    }

    private List<Fetch> fetchs;
    private List<FetchEntity> Responses;
    private List<FetchEntity> Errors;
    private Context mContext;
    private boolean isInternalFetching, forceFetchDone, forcedClose;

    private void init() {
        fetchs = new ArrayList();
        clearEntities();
    }

    private void clearEntities() {
        isInternalFetching = false;
        forcedClose = false;
        Responses = new ArrayList();
        Errors = new ArrayList();
    }

    public FetchUIManager(Context ctx) {
        this.mContext = ctx;
        this.forceFetchDone = false;
        init();
    }

    /**
     * Forza a que todos los fetch de datos se cumplan para llamar al metodo
     * onRequestsFetching en caso de ser llamados uno por uno
     *
     *
     */
    public void setForceFetchDone(boolean _forceFetchDone) {
        this.forceFetchDone = _forceFetchDone;
    }

    public int getFetchCount() {
        return this.fetchs.size();
    }

    public int getFetchCount(FetchResultType type) {
        switch (type) {
            case SUCCESS:
                return Responses.size();
            case PROCESSING:
                return fetchs.size() - (Responses.size() + Errors.size());
            case FAILED:
                return Errors.size();
        }
        return 0;
    }

    public boolean AllFetchDone() {
        return getFetchCount(FetchResultType.SUCCESS) == getFetchCount();
    }

    @SuppressLint("NewApi")
    private void processFetch(Fetch _fetch, boolean all) {
        _fetch.Call();
        CompletableFuture<String> response = _fetch.getResponseAsync();
        response.thenAccept(res -> {
            FetchEntity _entity = new FetchEntity(_fetch.key, res);
            addResponse(_entity);
            onResponseDone(_entity, null, all);
        }).exceptionally(error -> {
            error.printStackTrace();
            FetchEntity _entity = new FetchEntity(_fetch.key, null);
            FetchEntity _error = new FetchEntity(_fetch.key, error.getMessage());
            addResponse(_entity);
            addErrors(_error);
            onResponseDone(_entity, _error, all);
            return null;
        }).whenComplete((result, ex) -> { // Finally
            _fetch.clearResponse(); // Se limpia el CompletableFuture para eliminar la respuesta previa
        });
    }

    private void onResponseDone(FetchEntity _entity, FetchEntity _error, boolean all) {
        int totalResponses = Responses.size() + Errors.size();
        onFetchCurrentResult(_entity, _error);
        boolean allDone = totalResponses >= fetchs.size();
        if (!all || allDone) {
            ReturnResults();
        }
    }

    private void ReturnResults() {
        onRequestsFetching(false);
        onFetchResults(Responses, Errors);
        clearEntities();
    }

    public void ForceClose() {
        this.forcedClose = true;
        this.ReturnResults();
    }

    public boolean hasForcedClose() {
        return this.forcedClose;
    }

    @SuppressLint("NewApi")
    private void setFetchEntity(List<FetchEntity> FetchList, FetchEntity entity) {
        if (!FetchList.stream().anyMatch(e -> e.key.equals(entity.key))) {
            FetchList.add(entity);
        } else {
            ListIterator<FetchEntity> list = FetchList.listIterator();
            while (list.hasNext()) {
                FetchEntity currEntity = list.next();
                if (currEntity.key.equals(entity.key)) {
                    list.set(entity);
                }
            }
        }
    }

    private void addResponse(FetchEntity responseEntity) {
        setFetchEntity(Responses, responseEntity);
    }

    private void addErrors(FetchEntity errorEntity) {
        setFetchEntity(Errors, errorEntity);
    }

    @SuppressLint("NewApi")
    private Fetch getFetch(String key) {
        return fetchs.stream().filter(f -> key.equals(f.key)).findAny().orElse(null);
    }

    public void clear() {
        clearEntities();
    }

    public void CallAll() {
        if (fetchs.size() == 0)
            return;
        isInternalFetching = true;
        onRequestsFetching(true);
        for (Fetch _fetch : fetchs) {
            processFetch(_fetch, true);
        }
    }

    public void CallById(String key) {
        Fetch fetch = getFetch(key);
        if (fetch == null)
            return;
        this.isInternalFetching = true;
        onRequestsFetching(true);
        processFetch(fetch, this.forceFetchDone);
    }

    public Fetch addFetch(String key, FetchOptions options) throws Exception {
        if (isInternalFetching)
            throw new Exception(
                    "No se puede agregar una nueva llamada cuando se estan procesando los elementos ya existentes");
        Fetch fetch = new Fetch(key, options, mContext);
        fetchs.add(fetch);
        return fetch;
    }

    @Override
    public void addFetchs(FetchUIManager manager) {

    }

    @Override
    public void onFetchCurrentResult(FetchEntity entity, @Nullable FetchEntity error) {

    }

    @Override
    public void onFetchResults(List<FetchEntity> entities, List<FetchEntity> errors) {

    }

    @Override
    public void onRequestsFetching(boolean isFetching) {

    }
}
