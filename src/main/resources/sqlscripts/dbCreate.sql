CREATE TABLE `addresses`
(
    `id`      varchar(36) PRIMARY KEY,
    `number`  int          NOT NULL,
    `street`  varchar(255) NOT NULL,
    `city`    varchar(255) NOT NULL,
    `state`   varchar(255) NOT NULL,
    `country` varchar(255) NOT NULL,
    `zipCode` varchar(255) NOT NULL
);

CREATE TABLE `users`
(
    `id`                       varchar(36) PRIMARY KEY,
    `firstName`                varchar(255) NOT NULL,
    `lastName`                 varchar(255) NOT NULL,
    `email`                    varchar(255) NOT NULL UNIQUE,
    `phoneNumber`              varchar(20)  NOT NULL UNIQUE,
    `password`                 varchar(255) NOT NULL,
    `userType`                 varchar(255) NOT NULL,
    `currentAddressId`         varchar(36),  -- not null if user is customer
    `transportType`            varchar(25),  -- not null if user is driver
    `administratorCertificate` varchar(255), -- not null if user is local administrator
    FOREIGN KEY (`currentAddressId`) REFERENCES `addresses` (`id`)
);

CREATE TABLE `customer_addresses`
(
    `customerId` varchar(36) NOT NULL,
    `addressId`  varchar(36) NOT NULL,
    FOREIGN KEY (`customerId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`addressId`) REFERENCES `addresses` (`id`),
    PRIMARY KEY (`customerId`, `addressId`)
);

CREATE TABLE `locals`
(
    `id`              varchar(36) PRIMARY KEY,
    `administratorId` varchar(36)  NOT NULL,
    `name`            varchar(255) NOT NULL,
    `description`     varchar(255) NOT NULL,
    `addressId`       varchar(36)  NOT NULL,
    `phoneNumber`     varchar(20)  NOT NULL,
    `email`           varchar(255) NOT NULL,
    `type`            varchar(255) NOT NULL,
    `category`        varchar(255) NOT NULL,
    `openHour`        time         NOT NULL,
    `closeHour`       time         NOT NULL,
    `status`          varchar(10)  NOT NULL,
    FOREIGN KEY (`administratorId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`addressId`) REFERENCES `addresses` (`id`)
);

CREATE TABLE `products`
(
    `id`              varchar(36) PRIMARY KEY,
    `localId`         varchar(36)  NOT NULL,
    `name`            varchar(255) NOT NULL,
    `description`     varchar(255) NOT NULL,
    `price`           varchar(255) NOT NULL,
    `category`        varchar(255) NOT NULL,
    `quantity`        varchar(255) NOT NULL,
    `quantityMeasure` varchar(255) NOT NULL,
    FOREIGN KEY (`localId`) REFERENCES `locals` (`id`)
);

CREATE TABLE `commands`
(
    `id`          varchar(36) PRIMARY KEY,
    `customerId`  varchar(36) NOT NULL,
    `localId`     varchar(36) NOT NULL,
    `createdAt`   datetime    NOT NULL,
    `deliveredAt` datetime DEFAULT NULL,
    `status`      varchar(20) NOT NULL,
    FOREIGN KEY (`customerId`) REFERENCES `users` (`id`),
    FOREIGN KEY (`localId`) REFERENCES `locals` (`id`)
);

CREATE TABLE `command_products`
(
    `id`               varchar(36) PRIMARY KEY,
    `localId`          varchar(36)  NOT NULL,
    `name`             varchar(255) NOT NULL,
    `description`      varchar(255) NOT NULL,
    `price`            varchar(255) NOT NULL,
    `category`         varchar(255) NOT NULL,
    `quantity`         varchar(255) NOT NULL,
    `quantityMeasure`  varchar(255) NOT NULL,
    `commandId`        varchar(36)  NOT NULL,
    `numberOfProducts` int          NOT NULL,
    FOREIGN KEY (`localId`) REFERENCES `locals` (`id`),
    FOREIGN KEY (`commandId`) REFERENCES `commands` (`id`)
);