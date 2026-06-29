import { jwtDecode } from "jwt-decode";


export function getUserInfo(){

    const token = localStorage.getItem("token");

    if(!token){
        return null;
    }


    try{

        return jwtDecode(token);

    }catch(error){

        return null;

    }

}



export function logout(){

    localStorage.removeItem("token");

    window.location.href="/login";

}