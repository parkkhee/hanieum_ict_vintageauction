import React from "react"
import '../css/Header.css'
import ItemNavigation from "./ItemNavigation"
import Navigation from "./Navigation"

const Header =({ memberObj, refreshMember }) => {

    return (
        <div className="header-container">
            <h1 className="header-title">Vintage Action</h1>
            {/*네비게이션에 유저정보 전달하여 유저 정보에 따라 다르게 표시 */}
            <Navigation memberObj={memberObj} refreshMember={refreshMember}/>
            <ItemNavigation />
        </div>
    )

}

export default Header