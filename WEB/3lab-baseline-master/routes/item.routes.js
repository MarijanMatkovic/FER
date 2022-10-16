var express = require('express');
var router = express.Router();
var db = require('../db/index');

router.get('/:id([0-9]{1,10})', async function(req, res, next) {
    
    let id = parseInt(req.params.id);
    let item = (await db.query('SELECT * FROM inventory WHERE id = $1', [id])).rows[0];
    
    if (item) {

        let category = (await db.query('SELECT * FROM categories WHERE id = $1', [item.categoryid])).rows[0];
        
        let partners = (await db.query('SELECT * FROM partners WHERE partnerFor = $1', [item.id])).rows;

        res.render('item', {
            title: item.name,
            linkActive: 'order',
            item: item,
            category: category,
            index: id,
            partners: partners
        });

    } else {
        res.status(404).send("The item doesn't exist");
    }
});

module.exports = router;