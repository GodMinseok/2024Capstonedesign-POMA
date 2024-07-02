/*
app.post("/payments/verify", async  (req, res) => {

    const { data: { access_token } } = await axios({

        url: "https://api.iamport.kr/users/getToken",
        method: "post",
        headers: { "Content-Type": "application/json" },
        data: {
            imp_key: `${}`,
            imp_secret: `${}`
        }

    });
    const {} = req.body;
    const {data: {response}} = await axios ({
        url: `https://api.iamport.kr/payments/${}`,
        headers: {"Authorization" : access_token}
    });
    const { amount , name , merchant_uid } = response;
    const product = await Products.fineOne({name});

    if (product.price === amount) {
        shipPackage();
        res.sendStatus(200);
    } else {
        reportForgedPayment();
        res.sendStatus(400)
    }
});
*/
