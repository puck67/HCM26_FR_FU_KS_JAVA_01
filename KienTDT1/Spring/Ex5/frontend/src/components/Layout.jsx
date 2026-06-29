import Sidebar from "./Sidebar";
import Navbar from "./Navbar";


export default function Layout({children}){


    return (

        <div className="flex">


            <Sidebar/>


            <main className="flex-1 bg-gray-100 min-h-screen">


                <Navbar/>


                <section className="p-8">

                    {children}

                </section>


            </main>


        </div>


    )

}