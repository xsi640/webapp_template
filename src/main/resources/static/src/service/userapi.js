import { Request, Method } from './webapi';
import * as API from '../consts/api';

export const getUsers = (pageIndex, pageSize, resolve, reject) => {
    Request(API.getUsers, Method.GET, {
        pageIndex,
        pageSize
    }, resolve, reject);
}

export const saveUser = (user, resolve, reject) => {
    return Request(API.saveUser, Method.POST, {
        loginName: user.loginName,
        realName: user.realName,
        loginPwd: user.loginPwd,
        sex: user.sex,
        birthday: user.birthday,
        address: user.address,
    }, resolve, reject);
}

export const modifyUser = (user, resolve, reject) => {
    return Request(API.saveUser, Method.PUT, {
        id: user.id,
        realName: user.realName,
        loginPwd: user.loginPwd,
        sex: user.sex,
        birthday: user.birthday,
        address: user.address,
    }, resolve, reject)
}

export const deleteUser = (id, resolve, reject) => {
    let url = API.deleteUser.replace(/{id}/g, id);
    return Request(url, Method.DELETE, {}, resolve, reject)
}

export const checkUser = (loginName, resolve, reject) => {
    let url = API.checkUserName;
    return Request(url, Method.GET, { loginName: loginName }, resolve, reject)
}