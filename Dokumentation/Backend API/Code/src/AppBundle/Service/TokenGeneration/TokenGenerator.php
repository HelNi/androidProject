<?php
/**
 * Created by PhpStorm.
 * User: Admin
 * Date: 09.07.2017
 * Time: 18:10
 */

namespace AppBundle\Service\TokenGeneration;


use AppBundle\Entity\User;

class TokenGenerator implements TokenGeneratorInterface
{
    /**
     * @param User $user
     * @return string
     */
    public function generateToken(User $user)
    {
        return rtrim(strtr(base64_encode(random_bytes(32)), '+/', '-_'), '=');
    }
}