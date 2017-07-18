<?php
/**
 * User: nsh
 * Date: 07.07.2017
 * Time: 12:16
 */

namespace AppBundle\Security;


use FOS\UserBundle\Security\EmailUserProvider;

class UserProvider extends EmailUserProvider
{
    protected function findUser($username)
    {
        $user = parent::findUser($username);
        if (!$user) {
            $user = $this->userManager->findUserBy(['currentApiKey' => $username]);
        }

        return $user;
    }
}