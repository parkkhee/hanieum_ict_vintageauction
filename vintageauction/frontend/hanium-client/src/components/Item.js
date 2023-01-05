import React from "react"
import "../css/Item.css"
const Item =({ itemInfo, itemTitle }) => {
    const itemImage = itemInfo.storeFileNames[0];
    return (
        <div className="item-container">
            <img className="thumbnail" 
            src={itemImage ? (require(`../itemImages/${itemImage}`)):(require("../img/temp.png"))} />            
            <br/>
            <p className="item-title">{itemTitle}</p>
        </div>
    )

}

export default Item