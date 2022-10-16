var express = require('express');
var router = express.Router();
var db = require('../db/index');
var {body, validationResult} = require('express-validator');

router.get('/', async function(req, res, next) {

    let partners = (await db.query('SELECT * FROM partners ORDER BY id')).rows;

    res.render('partners', {
        title: 'Partners',
        linkActive: 'partners',
        partners: partners
    });
});

router.get('/update-partner/:id', async function(req, res, next) {

    let id = parseInt(req.params.id);

    let item = (await db.query('SELECT * FROM inventory WHERE id = $1', [id])).rows[0];

    let partners = (await db.query('SELECT * FROM partners WHERE id = $1', [item.id])).rows;

    if(item) {

        res.render('update-partner', {
            title: 'Update partner',
            linkActive: 'partners',
            item: item,
            itemID: item.id,
            partners: partners
        });

    }

    else res.status(404).send("The item you asked for does not exist!");


});

router.post('/update-partner/:id([0-9]+)',
    [
        body('name').trim().isLength({
            min: 1,
            max: 31
        }),
        body('owner_name').trim().isLength({
            min: 1,
            max: 31
        }),
        body('owner_surname').trim().isLength({
            min: 1,
            max: 31
        }),
        body('email').trim().isEmail(),
        body('partnersince').trim().isInt({
            min: 1935,
            max: 2022
        }).toInt,
        body('id').trim().notEmpty()
    ],
    async function(req, res, next) {

        let err = validationResult(req);

        let id = parseInt(req.body.id);

        console.log(req.body);

        console.log(err);

        if(err.isEmpty()) {
            try {
                await db.query(
                    'UPDATE partners SET (name = $1, owner_name = $2, owner_surname = $3, email = $4, partnerSince = $5, partnerfor = $6',
                    [req.body.name, req.body.owner_name, req.body.owner_surname, req.body.email, req.body.partnerSince, id]
                )

                res.redirect(`/item/${id}`)

            } catch (error) {

                console.log(error.message)

                res.render('error', {
                    title: "Update Partner",
                    linkActive: "partners",
                    errors: err.array(),
                    errDB: error.message,
                    itemID: item.id
                })

            }
        }
        else {
            res.render('error', {
                title: "Update Partner",
                linkActive: "partners",
                errors: err.array(),
                itemID: item.id
            })
        }
    
});

module.exports = router;