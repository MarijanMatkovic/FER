var express = require('express');
var router = express.Router();
var db = require('../db/index');

router.get('/', async function(req, res, next) {

    let categories = (await db.query('SELECT * FROM categories ORDER BY id')).rows;
    let inventory = (await db.query('SELECT * FROM inventory ORDER BY id')).rows;

    for(category of categories) {
        category.inventory = [];
        for(item of inventory)
            if(item.categoryid === category.id)
                category.inventory.push(item);
    }

    res.render('order', {
        title: 'Order!',
        linkActive: 'order',
        categories: categories
    });
});
module.exports = router;