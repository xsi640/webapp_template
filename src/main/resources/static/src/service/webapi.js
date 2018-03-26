import * as ObjectUtils from '../utils/objectutils'

const headers = { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8', };

export const Request = (url, method, parameters = {}, resolve, reject) => {
    ObjectUtils.fixObject(parameters);
    if (method === Method.GET) {
        url += '?';
        for (let key in parameters) {
            url += encodeURI(key) + '=' + encodeURI(parameters[key]) + '&';
        }
        url += 'd=' + new Date().getTime();
        fetch(url, { method: 'GET', headers: headers })
        .then(resp => resp.json())
        .then(json => resolve(json))
        .catch(err => reject(err));
    }else{
        url += '?d=' + new Date().getTime();
        let data = '';
        for (let key in parameters) {
            data += encodeURI(key) + '=' + encodeURI(parameters[key]) + '&'
        }
        fetch(url, { method: method, headers: headers, body: data })
            .then(resp => resp.json())
            .then(json => resolve(json))
            .catch(err => reject(err));
    }
}

export const Method = {
    GET: 'GET',
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE'
}