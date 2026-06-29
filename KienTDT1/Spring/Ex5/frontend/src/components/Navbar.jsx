import { getUserInfo, logout } from "../utils/auth";


export default function Navbar(){


    const user = getUserInfo();



    return (

        <header className="
            h-16 
            bg-white 
            shadow 
            flex 
            items-center 
            justify-between 
            px-8
        ">


            <div>

                Welcome,

                <span className="
                    font-bold 
                    ml-2
                ">
                    {user?.sub}
                </span>

            </div>



            <div className="flex items-center gap-5">


                



                <button

                onClick={logout}

                className="
                    bg-red-500
                    text-white
                    px-4
                    py-2
                    rounded
                    hover:bg-red-600
                ">

                    Logout

                </button>


            </div>


        </header>

    )

}