insert into addresses
values ("c6c5f2ed-82f8-410b-a752-005b3b6eac26", 2, "street new", "city", "state", "country", "zip");
insert into addresses
values ("5bd4c7f7-5b61-4c47-b7e3-d9e672878b76", 3, "street", "city", "state", "co", "zi");
insert into addresses
values ("e6abc4e0-d253-4c2a-9bae-f061b302f25a", 4, "street", "city", "state", "country", "zip");

insert into users
values ("15492443-c8c5-4c87-bd3b-7868dced7db8", "fname", "lname", "emailc", "phone1", "pass", "CUSTOMER",
        "c6c5f2ed-82f8-410b-a752-005b3b6eac26", null, null);
insert into users
values ("26b45609-ca72-48ef-a4ec-7326be930fa9", "fname", "lname", "emailla", "phone2 number", "new_pass",
        "LOCAL_ADMINISTRATOR", null, null, "certi");
insert into users
values ("0309109e-146b-4a80-ac26-cc12e17cd674", "delivery guy", "gut", "emaildp", "+23121", "pass", "DELIVERY_PERSON",
        null, "CAR", null);

insert into customer_addresses
values ("15492443-c8c5-4c87-bd3b-7868dced7db8", "c6c5f2ed-82f8-410b-a752-005b3b6eac26");
insert into customer_addresses
values ("15492443-c8c5-4c87-bd3b-7868dced7db8", "5bd4c7f7-5b61-4c47-b7e3-d9e672878b76");

insert into locals
values ("a0e5f445-b2b4-40a8-818d-15345fd15d78", "26b45609-ca72-48ef-a4ec-7326be930fa9", "name", "desc",
        "e6abc4e0-d253-4c2a-9bae-f061b302f25a", "phone", "email", "type", "category", "09:30:00", "20:00:00", "ACTIVE");
insert into locals
values ("a0e5f445-b2b4-40a8-818d-15345fd15d79", "26b45609-ca72-48ef-a4ec-7326be930fa9", "name", "fara produse",
        "e6abc4e0-d253-4c2a-9bae-f061b302f25a", "phone", "email", "type", "category", "09:30:00", "20:00:00", "ACTIVE");

insert into products
values ("0560d780-24e8-437b-ad9e-ad77e3255200", "a0e5f445-b2b4-40a8-818d-15345fd15d78", "name product",
        "description prod", "21", "category", "100", "kg");
insert into products
values ("0560d780-24e8-437b-ad9e-ad77e3255201", "a0e5f445-b2b4-40a8-818d-15345fd15d78", "name product1",
        "description prod", "21", "category", "100", "kg");
insert into products
values ("0560d780-24e8-437b-ad9e-ad77e3255202", "a0e5f445-b2b4-40a8-818d-15345fd15d78", "name product2",
        "description prod", "21", "category", "100", "kg");

insert into commands
values ("9a52ece6-e4cf-4d32-9742-5f3a4653c2a7", "15492443-c8c5-4c87-bd3b-7868dced7db8",
        "a0e5f445-b2b4-40a8-818d-15345fd15d78", "2022-05-14 00:13:59", "2022-05-14 02:24:31", "DELIVERED");

insert into command_products
values ("0560d780-24e8-437b-ad9e-ad77e3255200", "a0e5f445-b2b4-40a8-818d-15345fd15d78", "name product",
        "description prod", "21", "category", "100", "kg", "9a52ece6-e4cf-4d32-9742-5f3a4653c2a7", "1");
insert into command_products
values ("0560d780-24e8-437b-ad9e-ad77e3255201", "a0e5f445-b2b4-40a8-818d-15345fd15d78", "name product1",
        "description prod", "21", "category", "100", "kg", "9a52ece6-e4cf-4d32-9742-5f3a4653c2a7", "2");
insert into command_products
values ("0560d780-24e8-437b-ad9e-ad77e3255202", "a0e5f445-b2b4-40a8-818d-15345fd15d78", "name product",
        "description prod", "21", "category", "100", "kg", "9a52ece6-e4cf-4d32-9742-5f3a4653c2a7", "3");
